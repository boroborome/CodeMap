import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {CmWorkspaceSingle} from "../workspace-common/workspace-single.component";
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {NzModalService} from "ng-zorro-antd/modal";
import {EChartOption} from 'echarts';

@Component({
  selector: 'app-workspace-view',
  templateUrl: './workspace-view.component.html',
  styleUrls: ['./workspace-view.component.scss']
})
export class WorkspaceViewComponent extends CmWorkspaceSingle implements OnInit {
  chartOption: EChartOption = {
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line'
    }]
  }

  isCollapse = false;

  constructor(fb: FormBuilder,
              workSpaceService: CmWorkspaceService,
              route: ActivatedRoute,
              private message: NzMessageService,
              private router: Router,
              private modal: NzModalService,
  ) {
    super(fb, workSpaceService, route)
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  resetForm(): void {
    this.validateForm.reset();
  }

  toggleCollapse(): void {
    this.isCollapse = !this.isCollapse;
  }
}
