package com.vzoom.apocalypse.api.strategy.context;


import com.vzoom.apocalypse.api.strategy.ReadFeedbackFileStrategy;

import java.util.List;

/**
 * @author wans
 */
public class ReadFileContext {
   private ReadFeedbackFileStrategy fileStrategy;

   public ReadFileContext(ReadFeedbackFileStrategy strategy) {
       this.fileStrategy = strategy;
   }
   public List<String> contextInterface(String path) throws Exception {
       return fileStrategy.readFeedbackData(path);
   }
}
