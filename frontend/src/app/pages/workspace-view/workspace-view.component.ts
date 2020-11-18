import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-workspace-view',
  templateUrl: './workspace-view.component.html',
  styleUrls: ['./workspace-view.component.scss']
})
export class WorkspaceViewComponent implements OnInit {
  name: string;
  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.name = params['name'];
    });
  }

}
