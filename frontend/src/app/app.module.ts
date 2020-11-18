import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { IconsProviderModule } from './icons-provider.module';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { zh_CN } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
import {NzMessageModule} from "ng-zorro-antd/message";
import { NewWorkspaceComponent } from './pages/new-workspace/new-workspace.component';
import { ManageLibsComponent } from './pages/manage-libs/manage-libs.component';
import { WorkspaceViewComponent } from './pages/workspace-view/workspace-view.component';
import { WorkspaceSettingsComponent } from './pages/workspace-settings/workspace-settings.component';
import { NzFormModule } from 'ng-zorro-antd/form';
import {NzInputModule} from "ng-zorro-antd/input";
import {NzUploadModule} from "ng-zorro-antd/upload";

registerLocaleData(zh);

@NgModule({
  declarations: [
    AppComponent,
    NewWorkspaceComponent,
    ManageLibsComponent,
    WorkspaceViewComponent,
    WorkspaceSettingsComponent
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
  ],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [AppComponent]
})
export class AppModule { }
