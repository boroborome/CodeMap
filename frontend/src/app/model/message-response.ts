export class MessageResponse {
  data;
  msgs: Map<string, string[]>;

  static from(response): MessageResponse {
    const mr: MessageResponse = new MessageResponse();
    mr.data = response.data;
    mr.msgs = response.msgs;
    return mr;
  }

  isSuccess(): boolean {
    const errors: string[] = this.msgs.get("error");
    return errors == null || errors.length == 0;
  }

  errorMessage(): string {
    const errors: string[] = this.msgs.get("error");
    if (errors == null || errors.length == 0) {
      return "";
    }
    return errors.join(";");
  }
}
