{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = [
    pkgs.maven
    pkgs.temurin-bin-17
    pkgs.jdt-language-server
  ];
}
