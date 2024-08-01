import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class HWEasy {

    @Test
    fun easyHw(): Unit = runBlocking {
        val numbers = generateNumbers()
        val toFind = 10
        val toFindOther = 1000
        val firstList = async { findNumberInList(toFind, numbers) }
        val secondList = async { findNumberInList(toFindOther, numbers) }

        val foundNumbers = listOf(firstList.await(), secondList.await())
        foundNumbers.forEach {
            if (it != -1) {
                println("Your number $it found!")
            } else {
                println("Not found number $toFind || $toFindOther")
            }
        }
    }
}

suspend fun findNumberInList(toFind: Int, numbers: List<Int>): Int {
    delay(2000L)
    return numbers.firstOrNull { it == toFind } ?: -1
}

fun generateNumbers() = (0..10000).map {
    (0..100).random()
}