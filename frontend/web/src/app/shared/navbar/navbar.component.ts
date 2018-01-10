import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material';
import {LoginDialogComponent} from '../login-dialog/login-dialog.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  private sections;
  private sectionKeys;
  private loginDialog: MatDialogRef<LoginDialogComponent>;
  private username;

  constructor(private dialog: MatDialog) {
    this.sections = {};
    this.sectionKeys = [];
    this.username = null;
  }

  ngOnInit() {
    this.sections = {
      'publication': 'Publication',
      'admin': 'Admin'
    };
    this.sectionKeys = Object.keys(this.sections);
  }

  openLogin() {
    this.loginDialog = this.dialog.open(LoginDialogComponent, {
      disableClose: true, // can only close by click close button
    });
  }
}