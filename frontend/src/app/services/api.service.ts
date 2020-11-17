import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor() { }

  cmApi(relativeUrl: string): string {
    return `/api/${relativeUrl}`;
  }
}
