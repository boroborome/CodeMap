export class MessageResponse<T> {
  data: T;
  msgs; //: Map<string, string[]> | object;

  static from(response): MessageResponse<any> {
    const mr = new MessageResponse();
    mr.data = response.data;
    mr.msgs = response.msgs;
    return mr;
  }

  private getErrors(): string[] {
    if (this.msgs instanceof Map) {
      return this.msgs.get("error");
    } else if (this.msgs == null) {
      return [];
    } else {
      return this.msgs.error;
    }
  }

  isSuccess(): boolean {
    const errors: string[] = this.getErrors();
    return errors == null || errors.length == 0;
  }

  errorMessage(): string {
    const errors: string[] = this.getErrors();
    if (errors == null || errors.length == 0) {
      return "";
    }
    return errors.join(";");
  }
}
