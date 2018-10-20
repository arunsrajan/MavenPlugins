package org.singam.maven.plugin;

public class NamedLayoutInfos
{
    private Urls urls;

    private String name;

    public Urls getUrls ()
    {
        return urls;
    }

    public void setUrls (Urls urls)
    {
        this.urls = urls;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [urls = "+urls+", name = "+name+"]";
    }
}
