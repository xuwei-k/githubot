package githubot

import org.junit.Test
import org.junit.Assert._

class GithubotTest {
  @Test
  def escape(): Unit = {
    assertEquals(UserAction.escape("README.md @ foo.java of github.com:"), "README_md 🍥 foo_java of github_com:")
  }
}
