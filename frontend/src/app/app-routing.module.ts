import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {NewWorkspaceComponent} from "./pages/new-workspace/new-workspace.component";

const routes: Routes = [
  // { path: '', pathMatch: 'full', redirectTo: '/welcome' },
  { path: 'workspace/new', pathMatch: 'full', component: NewWorkspaceComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
