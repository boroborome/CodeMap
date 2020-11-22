import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {CmWorkspaceSingle} from "../workspace-common/workspace-single.component";
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {NzModalService} from "ng-zorro-antd/modal";
import {EChartOption, ECharts} from 'echarts';
import {ClassRelationService} from "../../services/class-relation.service";
import {ClassRelationFilter} from "../../model/class-relation-filter";

@Component({
  selector: 'app-workspace-view',
  templateUrl: './workspace-view.component.html',
  styleUrls: ['./workspace-view.component.scss']
})
export class WorkspaceViewComponent extends CmWorkspaceSingle implements OnInit {
  unknownTypeInfo = {
    symbol: ['none', 'arrow'],
    lineStyle: {
      type: 'dotted'
    }
  };
  typeMap = new Map([[
    'inherit', {
      symbol: ['none', 'triangle'],
      lineStyle: {
        type: 'solid'
      }
    }
  ], [
    'member', {
      symbol: ['none', 'arrow'],
      lineStyle: {
        type: 'solid'
      }
    }
  ], [
    'reference', {
      symbol: ['none', 'arrow'],
      lineStyle: {
        type: 'dashed'
      }
    }
  ], [
    'unknown', this.unknownTypeInfo
  ]
  ]);

  showLoading: boolean = false;
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
              private relationService: ClassRelationService,
              private message: NzMessageService,
              private router: Router,
              private modal: NzModalService,
  ) {
    super(fb, workSpaceService, route)
  }

  ngOnInit(): void {
    super.initWorkspace();
  }

  resetForm(): void {
    this.validateForm.reset();
  }

  toggleCollapse(): void {
    this.isCollapse = !this.isCollapse;
  }

  refreshRelationWithInput() {
    const filter: ClassRelationFilter = this.collectFilter();
    this.relationService.queryRelation(filter)
      .subscribe(relationResult => {
        this.showLoading = true;
        const graphData = relationResult.nodes;
        const graphLink = relationResult.relations
          .map(edge => {
            let typeInfo = this.typeMap.get(edge.relation);
            if (typeInfo == null) {
              typeInfo = this.unknownTypeInfo;
            }
            return {
              source: edge.classA,
              type: typeInfo,
              symbol: typeInfo.symbol,
              lineStyle: typeInfo.lineStyle,
              target: edge.classB
            };
          });
        this.chartOption = this.createOption(graphData, graphLink);
        this.showLoading = false;
      })

  }

  createOption(graphData, graphLinks): EChartOption {
    return {
      tooltip: {
        show: true,
        trigger: 'item',
        enterable: true,
        formatter: function (params, ticket, callback) {
          // @ts-ignore
          let data = params.data;
          if (data.id != null) {
            return data.id.replaceAll('/', '.');
          }
          return 'Source:' + data.source.replaceAll('/', '.')
            + '<br>Type:' + data.type
            + '<br>Target:' + data.target.replaceAll('/', '.');
        }
      },
      legend: {
        data: ['Other']
      },
      series: [{
        type: 'graph',
        layout: 'force',
        animation: false,
        label: {
          show: true,
          position: 'right',
          formatter: '{b}'
        },
        draggable: true,
        data: graphData,
        categories: [],
        force: {
          edgeLength: 100,
          repulsion: 400,
          gravity: 0.1
        },
        roam: true,
        edgeSymbol: ['none', 'arrow'],
        links: graphLinks
      }]
    };
  }

  private collectFilter(): ClassRelationFilter {
    const workspace = this.collectCwFromUi();
    const filter: ClassRelationFilter = new ClassRelationFilter();
    filter.includes = workspace.includes;
    filter.excludes = workspace.excludes;
    filter.relationTypes = workspace.relationTypes;
    filter.refCount = workspace.refCount;
    filter.selected = workspace.selected;
    filter.fileRanges = workspace.fileRanges;
    return filter;
  }
}
