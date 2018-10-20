package org.singam.maven.plugin.sobjects;

public class SalesforceObjects
{
    private Sobjects[] sobjects;

    private String encoding;

    private String maxBatchSize;

    public Sobjects[] getSobjects ()
    {
        return sobjects;
    }

    public void setSobjects (Sobjects[] sobjects)
    {
        this.sobjects = sobjects;
    }

    public String getEncoding ()
    {
        return encoding;
    }

    public void setEncoding (String encoding)
    {
        this.encoding = encoding;
    }

    public String getMaxBatchSize ()
    {
        return maxBatchSize;
    }

    public void setMaxBatchSize (String maxBatchSize)
    {
        this.maxBatchSize = maxBatchSize;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sobjects = "+sobjects+", encoding = "+encoding+", maxBatchSize = "+maxBatchSize+"]";
    }
}
