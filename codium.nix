let
  pkgs = import (fetchTarball("https://github.com/NixOS/nixpkgs/archive/929116e316068c7318c54eb4d827f7d9756d5e9c.tar.gz")) { };
  system = builtins.currentSystem;
  extensions =
    (import (builtins.fetchGit {
      url = "https://github.com/nix-community/nix-vscode-extensions";
      ref = "refs/heads/master";
      rev = "f506d6e24cc3626683185d81870a6e960864b370";
    })).extensions.${system};
in
pkgs.mkShell {
  buildInputs = [
  ] ++ (with pkgs; [

    (vscode-with-extensions.override {
        vscode = vscodium;
        vscodeExtensions = [
            vscode-extensions.gruntfuggly.todo-tree
            extensions.vscode-marketplace.vscjava.vscode-maven
            extensions.vscode-marketplace.redhat.java
        ];
    })
  ]);
}
