{
  description = "TeamSpeak 3 Status Page";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    systems.url = "github:nix-systems/default";
  };

  outputs = { self, nixpkgs, systems }:
    let
      eachSystem = nixpkgs.lib.genAttrs (import systems);
    in
    {

      packages = eachSystem (system: 
      let
        ts3status = nixpkgs.legacyPackages.${system}.callPackage ./package.nix { };
      in
      {
        ts3status = ts3status;
        default = ts3status;
      });

      devShells = eachSystem (system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
        in
        {
          default = pkgs.mkShell { buildInputs = [ pkgs.maven pkgs.temurin-bin-21 pkgs.jdt-language-server ]; };
        });
    };
}
