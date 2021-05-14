package dev.utils

object Environment:
  private val env         = sys.env ++ sys.props
  val InterfaceNameKey    = "INTERFACE_NAME"
  val AasInterfaceNameKey = "AAS_INTERFACE_NAME"
  val TmtLogHomeKey       = "TMT_LOG_HOME"

  val InterfaceName: String    = env.getOrElse(InterfaceNameKey, "en0")
  val AasInterfaceName: String = env.getOrElse(AasInterfaceNameKey, "en0")
  val TmtLogHome: String       = env.getOrElse(TmtLogHomeKey, "/tmp/tmt-mono")

  val get =
    Map(
      InterfaceNameKey    -> InterfaceName,
      AasInterfaceNameKey -> AasInterfaceName,
      TmtLogHomeKey       -> TmtLogHome
    )
