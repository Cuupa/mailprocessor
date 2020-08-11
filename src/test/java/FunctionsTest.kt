import com.cuupa.mailprocessor.functions.filterParallel
import com.cuupa.mailprocessor.functions.forEachParallel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FunctionsTest {

    @Test
    fun testForEachParallel(){
        runBlocking(Dispatchers.Default) {
            (1..1000).forEachParallel {
                delay(1000)
                println(it * 2) }
        }
    }

    @Test
    fun testFilterParallel(){
        runBlocking(Dispatchers.Default) {

            (1..1000).filterParallel {
                delay(1000)
                it%10 == 0
            }.forEach { println(it) }
        }
    }
}