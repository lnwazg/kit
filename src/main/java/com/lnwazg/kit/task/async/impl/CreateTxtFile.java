package com.lnwazg.kit.task.async.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;

import com.lnwazg.kit.anno.Comment;
import com.lnwazg.kit.task.async.SiriTask;

@Comment("新建文本文件，并往文件中写入指定内容")
public class CreateTxtFile implements SiriTask
{
    @Override
    public void execute(Object... params)
    {
        if (params.length == 2)
        {
            String dir = (String)params[0];
            String content = (String)params[1];
            try
            {
                FileUtils.writeStringToFile(new File(dir), content, CharEncoding.UTF_8);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
