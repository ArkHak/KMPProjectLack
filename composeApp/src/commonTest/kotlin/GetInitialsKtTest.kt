import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class GetInitialsKtTest {
    @Test
    fun testGetInitials() {
        val fullName = "Oleg Mysin"

        assertThat(getInitials(fullName)).isEqualTo("OM")
    }
}
