export class StringUtil {

  public static connectStr(strArray: string[]): string {
    if (strArray == null || strArray.length == 0) {
      return "";
    } else if (strArray.length == 1) {
      return strArray[0];
    } else {
      return strArray.join(",");
    }
  }

  public static splitStr(str: string): string[] {
    if (str == null || str.length == 0) {
      return [];
    }
    return str.split(",");
  }
}
