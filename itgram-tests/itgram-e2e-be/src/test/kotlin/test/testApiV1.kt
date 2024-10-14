package test

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import ru.itgram.api.v1.models.PublicationDebug
import ru.itgram.api.v1.models.PublicationSearchFilter
import ru.itgram.api.v1.models.PublicationUpdateObject
import test.action.v1.*

fun FunSpec.testApiV1(client: Client, prefix: String = "", debug: PublicationDebug = debugStubV1) {
    context("${prefix}v1") {
        test("Create Publication ok") {
            client.createPublication(debug = debug)
        }

        test("Read Publication ok") {
            val created = client.createPublication(debug = debug)
            client.readPublication(created.id, debug = debug).asClue {
                it shouldBe created
            }
        }

        test("Update Publication ok") {
            val created = client.createPublication(debug = debug)
            val updatePublication = PublicationUpdateObject(
                id = created.id,
                lock = created.lock,
                title = "Start of work",
                description = created.description,
                publicationCategory = created.publicationCategory,
                visibility = created.visibility,
            )
            client.updatePublication(updatePublication, debug = debug)
        }

        test("Delete Publication ok") {
            val created = client.createPublication(debug = debug)
            client.deletePublication(created, debug = debug)
        }

        test("Search Publication ok") {
            val created1 = client.createPublication(someCreatePublication.copy(title = "Start of work"), debug = debug)
            val created2 = client.createPublication(someCreatePublication.copy(title = "New article"), debug = debug)

            withClue("Start of work") {
                val results = client.searchPublication(search = PublicationSearchFilter(searchString = "Start of work"), debug = debug)
                results shouldExist { it.title == created1.title }
            }

            withClue("New article") {
                val results = client.searchPublication(search = PublicationSearchFilter(searchString = "article"), debug = debug)
                results shouldExist { it.title == created2.title }
            }
        }
    }

}
