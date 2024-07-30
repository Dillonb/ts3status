{ lib, pkgs, ... }:
let
  jdk_headless = pkgs.jdk17_headless;
  maven = pkgs.maven.override { jdk = jdk_headless; };
in
maven.buildMavenPackage {
  pname = "ts3status";
  version = "1.0-SNAPSHOT";
  src = lib.fileset.toSource {
    root = ./.;
    fileset = lib.fileset.unions [
      (lib.fileset.difference ./src ./src/main/resources/application.properties)
      ./pom.xml
    ];
  };
  mvnHash = "sha256-HvYOnhY4llpeke5y81K/AYlOEYnY+vCmx1UxaL2nHt4=";
  nativeBuildInputs = [ pkgs.makeWrapper ];
  installPhase = ''
    mkdir -p $out/bin $out/share/ts3status
    install -Dm644 target/ts3status-1.0-SNAPSHOT.jar $out/share/ts3status

    makeWrapper ${jdk_headless}/bin/java $out/bin/ts3status \
    --add-flags "-jar $out/share/ts3status/ts3status-1.0-SNAPSHOT.jar"
  '';
  meta = with lib; {
    description = "TeamSpeak 3 status page";
    homepage = "https://github.com/Dillonb/ts3status";
    license = licenses.mit;
    maintainers = with maintainers; [ Dillonb ];
  };
}
