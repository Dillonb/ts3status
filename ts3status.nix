{ config, lib, pkgs, ... }:
let
  user = "ts3status";
  group = "ts3status";
  cfg = config.services.ts3status;
in
{
  options = {
    services.ts3status = {
      enable = lib.mkOption {
        type = lib.types.bool;
        default = false;
        description = ''
          Whether to run the TeamSpeak 3 Status Page
        '';
      };

      configFilePath = lib.mkOption {
        type = lib.types.str;
        default = "/etc/ts3status/application.properties";
        description = ''
          Path to the application.properties file to run the service with
        '';
      };

      dataDir = lib.mkOption {
        type = lib.types.str;
        default = "/var/lib/ts3status";
        description = ''
          Path to where application.properties puts db.sqlite3
        '';
      };
    };
  };

  config =
    let
      ts3status = pkgs.callPackage ./package.nix { configFilePath = cfg.configFilePath; };
    in
    lib.mkIf cfg.enable {

      users.users.ts3status = {
        description = "ts3status TeamSpeak 3 Status Page";
        isNormalUser = true; # Allocate a user id >1000
        home = cfg.dataDir;
        group = group;
        createHome = true;
      };
      users.groups."${group}" = {};


      systemd.services.teamspeak3-server = {
        description = "ts3status TeamSpeak 3 Status Page";
        after = [ "network.target" ];
        wantedBy = [ "multi-user.target" ];

        serviceConfig = {
          ExecStart = "${ts3status}/bin/ts3status";
          WorkingDirectory = cfg.dataDir;
          User = user;
          Group = group;
          Restart = "on-failure";
        };
      };
    };
}
