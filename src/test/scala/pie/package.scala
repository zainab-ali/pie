import weaver._
import weaver.Expectations.Helpers

package object pie {

  def unimplemented(implicit pos: SourceLocation) = Helpers.failure("unimplemented")
}
