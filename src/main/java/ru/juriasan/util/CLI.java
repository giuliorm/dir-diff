package ru.juriasan.util;

public class CLI {

  private String firstDirectory;
  private String secondDirectory;
  private String resultDirectory;

  private static final String USAGE = "" +
      "Usage:\n\n" +
      "dirdiff dir1 dir2 dir3, where dir1, dir2 - paths to directories which will be compared, " +
      "dir3 - path to resulting directory.";

  public CLI(String...args) {
    if (args.length != 3) {
      System.out.println(USAGE);
      throw new RuntimeException();
    }

    this.firstDirectory = args[0];
    this.secondDirectory = args[1];
    this.resultDirectory = args[2];
  }

  public String getFirstDirectory() {
      return this.firstDirectory;
  }

  public String getSecondDirectory() {
      return this.secondDirectory;
  }

  public String getResultDirectory() {
      return this.resultDirectory;
  }
}
