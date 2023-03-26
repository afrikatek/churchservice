import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeague, NewLeague } from '../league.model';

export type PartialUpdateLeague = Partial<ILeague> & Pick<ILeague, 'id'>;

export type EntityResponseType = HttpResponse<ILeague>;
export type EntityArrayResponseType = HttpResponse<ILeague[]>;

@Injectable({ providedIn: 'root' })
export class LeagueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leagues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(league: NewLeague): Observable<EntityResponseType> {
    return this.http.post<ILeague>(this.resourceUrl, league, { observe: 'response' });
  }

  update(league: ILeague): Observable<EntityResponseType> {
    return this.http.put<ILeague>(`${this.resourceUrl}/${this.getLeagueIdentifier(league)}`, league, { observe: 'response' });
  }

  partialUpdate(league: PartialUpdateLeague): Observable<EntityResponseType> {
    return this.http.patch<ILeague>(`${this.resourceUrl}/${this.getLeagueIdentifier(league)}`, league, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeague>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeague[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLeagueIdentifier(league: Pick<ILeague, 'id'>): number {
    return league.id;
  }

  compareLeague(o1: Pick<ILeague, 'id'> | null, o2: Pick<ILeague, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeagueIdentifier(o1) === this.getLeagueIdentifier(o2) : o1 === o2;
  }

  addLeagueToCollectionIfMissing<Type extends Pick<ILeague, 'id'>>(
    leagueCollection: Type[],
    ...leaguesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leagues: Type[] = leaguesToCheck.filter(isPresent);
    if (leagues.length > 0) {
      const leagueCollectionIdentifiers = leagueCollection.map(leagueItem => this.getLeagueIdentifier(leagueItem)!);
      const leaguesToAdd = leagues.filter(leagueItem => {
        const leagueIdentifier = this.getLeagueIdentifier(leagueItem);
        if (leagueCollectionIdentifiers.includes(leagueIdentifier)) {
          return false;
        }
        leagueCollectionIdentifiers.push(leagueIdentifier);
        return true;
      });
      return [...leaguesToAdd, ...leagueCollection];
    }
    return leagueCollection;
  }
}
