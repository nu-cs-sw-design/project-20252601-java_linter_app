package domain;

class RedundantInterfacesCheck implements  LintCheck {

    @Override
    public String getName() {
        return "Redundant Interfaces Check";
    }

    @Override
    public String getDescription() {
        return "Detects interfaces that are redundantly declared because they are already implemented parent or any ancestor class";
    }
}
