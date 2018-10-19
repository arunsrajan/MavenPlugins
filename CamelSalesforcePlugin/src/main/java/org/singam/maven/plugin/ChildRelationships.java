package org.singam.maven.plugin;

public class ChildRelationships
{
    private String field;

    private String restrictedDelete;

    private String cascadeDelete;

    private String childSObject;

    private String junctionIdListName;

    private String relationshipName;

    private String deprecatedAndHidden;

    private String[] junctionReferenceTo;

    public String getField ()
    {
        return field;
    }

    public void setField (String field)
    {
        this.field = field;
    }

    public String getRestrictedDelete ()
    {
        return restrictedDelete;
    }

    public void setRestrictedDelete (String restrictedDelete)
    {
        this.restrictedDelete = restrictedDelete;
    }

    public String getCascadeDelete ()
    {
        return cascadeDelete;
    }

    public void setCascadeDelete (String cascadeDelete)
    {
        this.cascadeDelete = cascadeDelete;
    }

    public String getChildSObject ()
    {
        return childSObject;
    }

    public void setChildSObject (String childSObject)
    {
        this.childSObject = childSObject;
    }

    public String getJunctionIdListName ()
    {
        return junctionIdListName;
    }

    public void setJunctionIdListName (String junctionIdListName)
    {
        this.junctionIdListName = junctionIdListName;
    }

    public String getRelationshipName ()
    {
        return relationshipName;
    }

    public void setRelationshipName (String relationshipName)
    {
        this.relationshipName = relationshipName;
    }

    public String getDeprecatedAndHidden ()
    {
        return deprecatedAndHidden;
    }

    public void setDeprecatedAndHidden (String deprecatedAndHidden)
    {
        this.deprecatedAndHidden = deprecatedAndHidden;
    }

    public String[] getJunctionReferenceTo ()
    {
        return junctionReferenceTo;
    }

    public void setJunctionReferenceTo (String[] junctionReferenceTo)
    {
        this.junctionReferenceTo = junctionReferenceTo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [field = "+field+", restrictedDelete = "+restrictedDelete+", cascadeDelete = "+cascadeDelete+", childSObject = "+childSObject+", junctionIdListName = "+junctionIdListName+", relationshipName = "+relationshipName+", deprecatedAndHidden = "+deprecatedAndHidden+", junctionReferenceTo = "+junctionReferenceTo+"]";
    }
}