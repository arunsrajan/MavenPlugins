package org.singam.maven.plugin;

public class SupportedScopes
{
    private String name;

    private String label;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLabel ()
    {
        return label;
    }

    public void setLabel (String label)
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", label = "+label+"]";
    }
}
