import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.itgram.common.models.MkplPublication
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.common.models.MkplPublicationLock
import ru.itgram.common.models.MkplUserId
import ru.itgram.common.repo.*
import ru.itgram.repo.common.IRepoPublicationInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class PublicationRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : PublicationRepoBase(), IRepoPublication, IRepoPublicationInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, PublicationEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<MkplPublication>) = ads.map { ad ->
        val entity = PublicationEntity(ad)
        require(entity.id != null)
        cache.put(entity.id, entity)
        ad
    }

    override suspend fun createPublication(rq: DbPublicationRequest): IDbPublicationResponse = tryPublicationMethod {
        val key = randomUuid()
        val publication = rq.publication.copy(id = MkplPublicationId(key), lock = MkplPublicationLock(randomUuid()))
        val entity = PublicationEntity(publication)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbPublicationResponseOk(publication)
    }

    override suspend fun readPublication(rq: DbPublicationIdRequest): IDbPublicationResponse = tryPublicationMethod {
        val key = rq.id.takeIf { it != MkplPublicationId.NONE }?.asString() ?: return@tryPublicationMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbPublicationResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updatePublication(rq: DbPublicationRequest): IDbPublicationResponse = tryPublicationMethod {
        val rqPublication = rq.publication
        val id = rqPublication.id.takeIf { it != MkplPublicationId.NONE } ?: return@tryPublicationMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldPublication = cache.get(key)?.toInternal()
            when {
                oldPublication == null -> errorNotFound(id)
                else -> {
                    val newAd = rqPublication.copy()
                    val entity = PublicationEntity(newAd)
                    cache.put(key, entity)
                    DbPublicationResponseOk(newAd)
                }
            }
        }
    }


    override suspend fun deletePublication(rq: DbPublicationIdRequest): IDbPublicationResponse = tryPublicationMethod {
        val id = rq.id.takeIf { it != MkplPublicationId.NONE } ?: return@tryPublicationMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldPublication = cache.get(key)?.toInternal()
            when {
                oldPublication == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbPublicationResponseOk(oldPublication)
                }
            }
        }
    }

    override suspend fun searchPublication(rq: DbPublicationFilterRequest): IDbPublicationsResponse = tryPublicationsMethod {
        val result: List<MkplPublication> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MkplUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.publicationCategory?.let {
                    it.name == entry.value.publicationCategory
                } ?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbPublicationsResponseOk(result)
    }
}
