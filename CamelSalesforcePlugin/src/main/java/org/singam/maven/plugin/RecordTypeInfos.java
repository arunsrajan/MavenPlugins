package org.singam.maven.plugin;

public class RecordTypeInfos
{
    private String recordTypeId;

    private Urls urls;

    private String name;

    private String available;

    private String defaultRecordTypeMapping;

    private String master;

    public String getRecordTypeId ()
    {
        return recordTypeId;
    }

    public void setRecordTypeId (String recordTypeId)
    {
        this.recordTypeId = recordTypeId;
    }

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

    public String getAvailable ()
    {
        return available;
    }

    public void setAvailable (String available)
    {
        this.available = available;
    }

    public String getDefaultRecordTypeMapping ()
    {
        return defaultRecordTypeMapping;
    }

    public void setDefaultRecordTypeMapping (String defaultRecordTypeMapping)
    {
        this.defaultRecordTypeMapping = defaultRecordTypeMapping;
    }

    public String getMaster ()
    {
        return master;
    }

    public void setMaster (String master)
    {
        this.master = master;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [recordTypeId = "+recordTypeId+", urls = "+urls+", name = "+name+", available = "+available+", defaultRecordTypeMapping = "+defaultRecordTypeMapping+", master = "+master+"]";
    }
}