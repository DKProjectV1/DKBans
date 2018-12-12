package ch.dkrieger.bansystem.lib.utils;

public class TabCompleteOption {

    private String permission, option;

    public TabCompleteOption(String permission, String option) {
        this.permission = permission;
        this.option = option;
    }
    public String getPermission() {
        return permission;
    }
    public String getOption() {
        return option;
    }
}

