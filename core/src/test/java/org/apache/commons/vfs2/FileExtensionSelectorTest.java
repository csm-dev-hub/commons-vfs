package org.apache.commons.vfs2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileExtensionSelectorTest
{
    private static FileObject BaseFolder;

    private static int FileCount;

    private static int ExtensionCount;

    private static int FilePerExtensionCount;

    /**
     * Creates a RAM FS.
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        BaseFolder = VFS.getManager().resolveFile("ram://" + FileExtensionSelectorTest.class.getName());
        BaseFolder.resolveFile("a.htm").createFile();
        BaseFolder.resolveFile("a.html").createFile();
        BaseFolder.resolveFile("a.xhtml").createFile();
        BaseFolder.resolveFile("b.htm").createFile();
        BaseFolder.resolveFile("b.html").createFile();
        BaseFolder.resolveFile("b.xhtml").createFile();
        BaseFolder.resolveFile("c.htm").createFile();
        BaseFolder.resolveFile("c.html").createFile();
        BaseFolder.resolveFile("c.xhtml").createFile();
        FileCount = BaseFolder.getChildren().length;
        ExtensionCount = 3;
        FilePerExtensionCount = 3;
    }

    /**
     * Deletes RAM FS files.
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception
    {
        if (BaseFolder != null)
        {
            BaseFolder.delete(Selectors.SELECT_ALL);
        }
    }

    /**
     * Tests an empty selector.
     * 
     * @throws Exception
     */
    @Test
    public void testEmpty() throws Exception
    {
        FileSelector selector0 = new FileExtensionSelector();
        FileObject[] foList = FileExtensionSelectorTest.BaseFolder.findFiles(selector0);
        Assert.assertEquals(0, foList.length);
    }

    /**
     * Tests many extensions at once.
     * 
     * @throws Exception
     */
    @Test
    public void testManyExtensions() throws Exception
    {
        FileObject[] foList = FileExtensionSelectorTest.BaseFolder.findFiles(Selectors.SELECT_FILES);
        Assert.assertTrue(foList.length > 0);
        // gather file extensions.
        Set<String> extensionSet = new HashSet<String>();
        for (FileObject fo : foList)
        {
            extensionSet.add(fo.getName().getExtension());
        }
        Assert.assertTrue(extensionSet.size() > 0);
        Assert.assertEquals(ExtensionCount, extensionSet.size());
        // check all unique extensions
        FileSelector selector = new FileExtensionSelector(extensionSet);
        FileObject[] list = FileExtensionSelectorTest.BaseFolder.findFiles(selector);
        Assert.assertEquals(FileCount, list.length);
    }

    /**
     * Tests a null selector.
     * 
     * @throws Exception
     */
    @Test
    public void testNullCollection() throws Exception
    {
        FileSelector selector0 = new FileExtensionSelector((Collection<String>) null);
        FileObject[] foList = FileExtensionSelectorTest.BaseFolder.findFiles(selector0);
        Assert.assertEquals(0, foList.length);
    }

    /**
     * Tests a null selector.
     * 
     * @throws Exception
     */
    @Test
    public void testNullString() throws Exception
    {
        FileSelector selector0 = new FileExtensionSelector((String) null);
        FileObject[] foList = FileExtensionSelectorTest.BaseFolder.findFiles(selector0);
        Assert.assertEquals(0, foList.length);
    }

    /**
     * Tests a one extension selector.
     * 
     * @throws Exception
     */
    @Test
    public void testOneExtension() throws Exception
    {
        FileObject[] foList = FileExtensionSelectorTest.BaseFolder.findFiles(Selectors.SELECT_FILES);
        Assert.assertTrue(foList.length > 0);
        // gather file extensions.
        Set<String> extensionSet = new HashSet<String>();
        for (FileObject fo : foList)
        {
            extensionSet.add(fo.getName().getExtension());
        }
        Assert.assertEquals(ExtensionCount, extensionSet.size());
        // check each extension
        for (String extension : extensionSet)
        {
            FileSelector selector = new FileExtensionSelector(extension);
            FileObject[] list = FileExtensionSelectorTest.BaseFolder.findFiles(selector);
            Assert.assertEquals(FilePerExtensionCount, list.length);
        }
        // check each file against itself
        for (FileObject fo : foList)
        {
            FileSelector selector = new FileExtensionSelector(fo.getName().getExtension());
            FileObject[] list = FileExtensionSelectorTest.BaseFolder.findFiles(selector);
            Assert.assertEquals(FilePerExtensionCount, list.length);
        }
    }

}