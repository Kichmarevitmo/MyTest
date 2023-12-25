package org.example;

public class AS_ADDR_OBJ_Object {

    private String objectId;
    private String name;
    private String typeName;
    private String startDate;
    private String endDate;
    private String isActual;
    private String isActive;

    public AS_ADDR_OBJ_Object(String objectId, String name, String typeName, String startDate, String endDate, String isActual, String isActive) {
        this.objectId = objectId;
        this.name = name;
        this.typeName = typeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActual = isActual;
        this.isActive = isActive;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getIsActual() {
        return isActual;
    }

    public String getIsActive() {
        return isActive;
    }
}
