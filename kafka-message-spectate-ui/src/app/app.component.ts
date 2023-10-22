import { HttpClient } from '@angular/common/http';
import { Component, ViewChild, OnInit } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { CellClickedEvent, ColDef, GridReadyEvent, GridApi } from 'ag-grid-community';
import { Observable } from 'rxjs';
import { KafkaConfig } from './domain/kafkaconfig';
import { Config } from './domain/kafkaconfig';
import { FormControl, FormGroup } from "@angular/forms";
import { ThemePalette } from '@angular/material/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent{
  private gridApi!: GridApi<any>;

  // Each Column Definition results in one Column.
  public columnDefs: ColDef[] = [
    //{ field: 'count'}
    //,
    //{ field: 'messages'}

    {headerName: 'Key', field: 'key', sortable: true},
    {headerName: 'Value', field: 'value', flex: 1, sortable: true}
  ];

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true,
    suppressSizeToFit: true
  };
  
  // Data that gets displayed in the grid
  public rows!: Observable<any[]>;
  public kafkaConfig: any;
  public selectedConfig = new Config();
  public selectedConsumerGroup= new String();
  public selectedTopic  = new String();
  public message = new String();
  public fetchBy = "date";
  public hideFetchByOffset = true; // by default display fetch by offset
  public hideFetchByDate = false;
  public maxPollTimeInSeconds = 10;
  public selectedOffset = 100;
  public selectedTime = "00:00";
  public selectedDate = new Date();

  public noRowsTemplate;
  public loadingTemplate;



  // For accessing the Grid's API
  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;

  constructor(private http: HttpClient) {
    this.loadingTemplate =
      `<span class="ag-overlay-loading-center">Loading data...</span>`;
    this.noRowsTemplate =
      `<span class="ag-overlay-loading-center">No rows to show</span>`;
  }

  // Example load data from server
  onGridReady(params: GridReadyEvent) {
    this.gridApi = params.api;
    this.http
      .get<Response>('http://localhost:8080/spectate/consumer/config')
      .subscribe(res => {
        this.kafkaConfig = res;
      });

      this.gridApi.showNoRowsOverlay();
  }

  // Example using Grid's API
  getKafkaMessages(): void {
    let url = '';
    if(this.validateForm()){
      
      this.message = 'Fetching 1000(default) messages from: '
                      + this.selectedConfig.host + ', '
                      + this.selectedConsumerGroup + ', '
                      + this.selectedTopic;
      if(!this.hideFetchByOffset){
        url = 'http://localhost:8080/spectate/poll-messages-by-offset/' 
        + this.selectedConfig.host + '/'
        + this.selectedConsumerGroup + '/'
        + this.selectedTopic + '/'
        + this.selectedOffset + '/'
        + this.maxPollTimeInSeconds;
       
      } else if(!this.hideFetchByDate) {
        url = 'http://localhost:8080/spectate/poll-messages-by-date/' 
        + this.selectedConfig.host + '/'
        + this.selectedConsumerGroup + '/'
        + this.selectedTopic + '/'
        + JSON.stringify(this.selectedDate).substring(1, 11) + 'T'+ this.selectedTime+':00/'
        + this.maxPollTimeInSeconds;
      } else {
        this.message = 'Operation not supported. Check the options';
        return;
      }
      console.log(url);
      this.rows = this.http.get<any[]>(url);
    } 
  }

  validateForm(): boolean {
    if (this.selectedConfig.host && this.selectedConfig.host.length <=0) {
      this.message = "Choose host";
      return false;
    }

    if (this.selectedConsumerGroup && this.selectedConsumerGroup.length <=0) {
      this.message = "Choose consumer group";
      return false;
    }
    if (this.selectedTopic && this.selectedTopic.length <=0) {
      this.message = "Choose topic";
      return false;
    }
    if (this.selectedTopic && this.selectedTopic.length <=0) {
      this.message = "Choose topic";
      return false;
    }
    if (!this.hideFetchByOffset && this.selectedOffset <=0) {
      this.message = "Invalid offset. Valid offsets are greater than 0";
      return false;
    }
    if (!this.hideFetchByDate && JSON.stringify(this.selectedDate).length == 0) {
      this.message = "Invalid date";
      return false;
    }

    if (this.maxPollTimeInSeconds <=0 && this.maxPollTimeInSeconds >300) {
      this.message = "Invalid poll time. Poll time  > 0 && <=300";
      return false;
    }

    return true;
  }

  fetchByChange() :  void {
    if(this.fetchBy=="offset"){
      this.hideFetchByOffset = false;
      this.hideFetchByDate = true;
    } else {
      this.hideFetchByOffset = true;
      this.hideFetchByDate = false;
    }
  }
}