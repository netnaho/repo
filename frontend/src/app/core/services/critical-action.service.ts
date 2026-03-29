import { Injectable } from '@angular/core';
import { HttpContext } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { SKIP_ERROR_TOAST } from '../interceptors/http-context.tokens';
import { CriticalActionRequestModel } from '../models/critical-action.models';

@Injectable({ providedIn: 'root' })
export class CriticalActionService {
  private readonly silentContext = new HttpContext().set(SKIP_ERROR_TOAST, true);

  constructor(private readonly apiService: ApiService) {}

  list(): Observable<CriticalActionRequestModel[]> {
    return this.apiService.get<CriticalActionRequestModel[]>('/critical-actions', { context: this.silentContext });
  }

  get(id: number): Observable<CriticalActionRequestModel> {
    return this.apiService.get<CriticalActionRequestModel>(`/critical-actions/${id}`, { context: this.silentContext });
  }

  create(body: unknown): Observable<CriticalActionRequestModel> {
    return this.apiService.post<CriticalActionRequestModel>('/critical-actions', body);
  }

  decide(id: number, body: unknown): Observable<CriticalActionRequestModel> {
    return this.apiService.post<CriticalActionRequestModel>(`/critical-actions/${id}/decision`, body);
  }
}
