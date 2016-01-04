/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.arizona.cs.hsyndicate.hadoop.connector.input;

import edu.arizona.cs.hsyndicate.fs.SyndicateFSPath;
import edu.arizona.cs.hsyndicate.hadoop.connector.util.HSyndicateCompressionCodecUtil;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class HSyndicateTextInputFormat extends HSyndicateFileInputFormat<LongWritable, Text> {

    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        if (!(split instanceof HSyndicateInputSplit)) {
            throw new IllegalStateException("Creation of a new RecordReader requires a HSyndicateInputSplit instance.");
        }
        
        String delimiter = context.getConfiguration().get("textinputformat.record.delimiter");
        byte[] recordDelimiterBytes = null;
        if (null != delimiter) {
            recordDelimiterBytes = delimiter.getBytes();
        }
        return new HSyndicateLineRecordReader(recordDelimiterBytes);
    }
    
    @Override
    protected boolean isSplitable(JobContext context, SyndicateFSPath path) {
        Configuration conf = context.getConfiguration();
        CompressionCodec codec = HSyndicateCompressionCodecUtil.getCompressionCodec(conf, path);
        return codec == null;
    }
}
