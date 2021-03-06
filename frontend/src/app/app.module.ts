import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {IconsProviderModule} from './icons-provider.module';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NZ_I18N, zh_CN} from 'ng-zorro-antd/i18n';
import {registerLocaleData} from '@angular/common';
import zh from '@angular/common/locales/zh';
import {NzMessageModule} from "ng-zorro-antd/message";
import {NewWorkspaceComponent} from './pages/new-workspace/new-workspace.component';
import {ManageLibsComponent} from './pages/manage-libs/manage-libs.component';
import {WorkspaceViewComponent} from './pages/workspace-view/workspace-view.component';
import {WorkspaceSettingsComponent} from './pages/workspace-settings/workspace-settings.component';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzInputModule} from "ng-zorro-antd/input";
import {NzUploadModule} from "ng-zorro-antd/upload";
import {BackendTaskComponent} from './pages/backend-task/backend-task.component';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzCheckboxModule} from "ng-zorro-antd/checkbox";
import {NzInputNumberModule} from 'ng-zorro-antd/input-number';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {SuccessResultComponent} from './pages/success-result/success-result.component';
import {NzResultModule} from 'ng-zorro-antd/result';
import {NzButtonModule} from "ng-zorro-antd/button";
import { NgxEchartsModule } from 'ngx-echarts';

registerLocaleData(zh);

@NgModule({
  declarations: [
    AppComponent,
    NewWorkspaceComponent,
    ManageLibsComponent,
    WorkspaceViewComponent,
    WorkspaceSettingsComponent,
    BackendTaskComponent,
    SuccessResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    IconsProviderModule,
    NzLayoutModule,
    NzMenuModule,
    NzMessageModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    NzFormModule,
    NzInputModule,
    NzUploadModule,
    NzTableModule,
    NzCheckboxModule,
    NzInputNumberModule,
    NzModalModule,
    NzResultModule,
    NzButtonModule,
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts')
    }),
  ],
  providers: [{provide: NZ_I18N, useValue: zh_CN}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
