package org.singam.maven.plugin;

public class PicklistValues
{
    private String value;

    private String active;

    private String validFor;

    private String label;

    private String defaultValue;

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getActive ()
    {
        return active;
    }

    public void setActive (String active)
    {
        this.active = active;
    }

    public String getValidFor ()
    {
        return validFor;
    }

    public void setValidFor (String validFor)
    {
        this.validFor = validFor;
    }

    public String getLabel ()
    {
        return label;
    }

    public void setLabel (String label)
    {
        this.label = label;
    }

    public String getDefaultValue ()
    {
        return defaultValue;
    }

    public void setDefaultValue (String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [value = "+value+", active = "+active+", validFor = "+validFor+", label = "+label+", defaultValue = "+defaultValue+"]";
    }
}
