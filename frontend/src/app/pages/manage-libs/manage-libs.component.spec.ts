import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageLibsComponent } from './manage-libs.component';

describe('ManageLibsComponent', () => {
  let component: ManageLibsComponent;
  let fixture: ComponentFixture<ManageLibsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageLibsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageLibsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
