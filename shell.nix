let
  pkgs = import (fetchTarball("https://github.com/NixOS/nixpkgs/archive/929116e316068c7318c54eb4d827f7d9756d5e9c.tar.gz")) {};
  buildInputs = (with pkgs; [
    jdk17
    maven
    antlr4_9
  ]);
in
pkgs.mkShell {
  inherit buildInputs;
  JAVA_HOME = "${pkgs.jdk11}";
}
