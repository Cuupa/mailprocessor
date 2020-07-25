import org.junit.Test

class StringTest {
    @Test
    fun test() {
        val joinToString = listOf("a", "b", "c").joinToString("_", "", "")
        println(joinToString)
    }
}