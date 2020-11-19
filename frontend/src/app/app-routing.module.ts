import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {NewWorkspaceComponent} from "./pages/new-workspace/new-workspace.component";
import {ManageLibsComponent} from "./pages/manage-libs/manage-libs.component";
import {WorkspaceViewComponent} from "./pages/workspace-view/workspace-view.component";
import {WorkspaceSettingsComponent} from "./pages/workspace-settings/workspace-settings.component";
import {BackendTaskComponent} from "./pages/backend-task/backend-task.component";
import {WelcomeComponent} from "./pages/welcome/welcome.component";
import {SuccessResultComponent} from "./pages/success-result/success-result.component";

const routes: Routes = [
  // { path: '', pathMatch: 'full', redirectTo: '/welcome' },
  { path: 'welcome', pathMatch: 'full', component: WelcomeComponent },
  { path: 'success-result', pathMatch: 'full', component: SuccessResultComponent },
  { path: 'workspace/new', pathMatch: 'full', component: NewWorkspaceComponent },
  { path: 'manage-libs', pathMatch: 'full', component: ManageLibsComponent },
  { path: 'backend-tasks', pathMatch: 'full', component: BackendTaskComponent },
  { path: 'workspace/view/:id', pathMatch: 'full', component: WorkspaceViewComponent },
  { path: 'workspace/settings/:id', pathMatch: 'full', component: WorkspaceSettingsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
