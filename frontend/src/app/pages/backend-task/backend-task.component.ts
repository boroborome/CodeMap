import {Component, OnInit} from '@angular/core';
import {NzMessageService} from "ng-zorro-antd/message";
import {Router} from "@angular/router";
import {BackendTaskService} from "../../services/backend-task.service";
import {BackendTask} from "../../model/backend-task";
import {MessageResponse} from "../../model/message-response";

@Component({
  selector: 'app-backend-task',
  templateUrl: './backend-task.component.html',
  styleUrls: ['./backend-task.component.scss']
})
export class BackendTaskComponent implements OnInit {
  listOfData: BackendTask[] = [];

  constructor(private backendTaskService: BackendTaskService,
              private message: NzMessageService,
              private router: Router,
              ) { }

  ngOnInit(): void {
    this.reloadTasks();
  }
  private reloadTasks() {
    this.backendTaskService.queryAllTasks()
      .subscribe(tasks => {
          this.listOfData = tasks;
      });
  }
}
