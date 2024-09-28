import ru.itgram.biz.general.initStatus
import ru.itgram.biz.general.operation
import ru.itgram.biz.general.stubs
import ru.itgram.common.MkplContext
import ru.itgram.common.MkplCorSettings
import ru.itgram.common.models.MkplCommand
import ru.itgram.common.models.MkplPublicationId
import ru.itgram.cor.rootChain
import ru.itgram.cor.worker
import ru.itgram.biz.stubs.*
import ru.itgram.biz.validation.*
import ru.itgram.common.models.MkplPublicationLock

@Suppress("unused", "RedundantSuspendModifier")
class MkplPublicationProcessor(
    private val corSettings: MkplCorSettings = MkplCorSettings.NONE
) {
    suspend fun exec(ctx: MkplContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MkplContext> {
        initStatus("Инициализация статуса")

        operation("Создание публикации", MkplCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в publicationValidating") { publicationValidating = publicationRequest.deepCopy() }
                worker("Очистка id") { publicationValidating.id = MkplPublicationId.NONE }
                worker("Очистка заголовка") { publicationValidating.title = publicationValidating.title.trim() }
                worker("Очистка описания") { publicationValidating.description = publicationValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")

                finishPublicationValidation("Завершение проверок")
            }
        }
        operation("Получить публикацию", MkplCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в publicationValidating") { publicationValidating = publicationRequest.deepCopy() }
                worker("Очистка id") { publicationValidating.id = MkplPublicationId(publicationValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishPublicationValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Изменить публикацию", MkplCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в publicationValidating") { publicationValidating = publicationRequest.deepCopy() }
                worker("Очистка id") { publicationValidating.id = MkplPublicationId(publicationValidating.id.asString().trim()) }
                worker("Очистка lock") { publicationValidating.lock = MkplPublicationLock(publicationValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { publicationValidating.title = publicationValidating.title.trim() }
                worker("Очистка описания") { publicationValidating.description = publicationValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishPublicationValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить публикацию", MkplCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в publicationValidating") {
                    publicationValidating = publicationRequest.deepCopy()
                }
                worker("Очистка id") { publicationValidating.id = MkplPublicationId(publicationValidating.id.asString().trim()) }
                worker("Очистка lock") { publicationValidating.lock = MkplPublicationLock(publicationValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishPublicationValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск публикаций", MkplCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в publicationFilterValidating") { publicationFilterValidating = publicationFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishAdFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}
