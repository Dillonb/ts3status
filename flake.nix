{
  description = "TeamSpeak 3 Status Page";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    systems.url = "github:nix-systems/default";
  };

  outputs = { self, nixpkgs, systems }: let
    eachSystem = nixpkgs.lib.genAttrs (import systems);
  in {

    # I don't think I need this, since we'll always be overriding the config path
    packages = eachSystem (system: {
        ts3status = nixpkgs.legacyPackages.${system}.callPackage ./package.nix {};
        });

    nixosModules.ts3status = ./ts3status.nix;
    nixosModules.default = self.nixosModules.ts3status;

    devShells = eachSystem (system: let
          pkgs = nixpkgs.legacyPackages.${system};
        in {
          default = pkgs.mkShell { buildInputs = [ pkgs.maven pkgs.temurin-bin-17 pkgs.jdt-language-server ]; };
        });
  };
}
