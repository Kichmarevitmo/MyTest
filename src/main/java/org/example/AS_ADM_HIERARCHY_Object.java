package org.example;

public class AS_ADM_HIERARCHY_Object {
    private String objectId;
    private String parentObjectId;

    public AS_ADM_HIERARCHY_Object(String objectId, String parentObjectId) {
        this.objectId = objectId;
        this.parentObjectId = parentObjectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getParentObjectId() {
        return parentObjectId;
    }

    public void setParentObjectId(String parentObjectId) {
        this.parentObjectId = parentObjectId;
    }
}
