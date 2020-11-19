import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BackendTaskComponent } from './backend-task.component';

describe('BackendTaskComponent', () => {
  let component: BackendTaskComponent;
  let fixture: ComponentFixture<BackendTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BackendTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BackendTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
