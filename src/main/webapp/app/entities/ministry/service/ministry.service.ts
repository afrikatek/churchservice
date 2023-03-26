import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMinistry, NewMinistry } from '../ministry.model';

export type PartialUpdateMinistry = Partial<IMinistry> & Pick<IMinistry, 'id'>;

export type EntityResponseType = HttpResponse<IMinistry>;
export type EntityArrayResponseType = HttpResponse<IMinistry[]>;

@Injectable({ providedIn: 'root' })
export class MinistryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ministries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ministry: NewMinistry): Observable<EntityResponseType> {
    return this.http.post<IMinistry>(this.resourceUrl, ministry, { observe: 'response' });
  }

  update(ministry: IMinistry): Observable<EntityResponseType> {
    return this.http.put<IMinistry>(`${this.resourceUrl}/${this.getMinistryIdentifier(ministry)}`, ministry, { observe: 'response' });
  }

  partialUpdate(ministry: PartialUpdateMinistry): Observable<EntityResponseType> {
    return this.http.patch<IMinistry>(`${this.resourceUrl}/${this.getMinistryIdentifier(ministry)}`, ministry, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMinistry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMinistry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMinistryIdentifier(ministry: Pick<IMinistry, 'id'>): number {
    return ministry.id;
  }

  compareMinistry(o1: Pick<IMinistry, 'id'> | null, o2: Pick<IMinistry, 'id'> | null): boolean {
    return o1 && o2 ? this.getMinistryIdentifier(o1) === this.getMinistryIdentifier(o2) : o1 === o2;
  }

  addMinistryToCollectionIfMissing<Type extends Pick<IMinistry, 'id'>>(
    ministryCollection: Type[],
    ...ministriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ministries: Type[] = ministriesToCheck.filter(isPresent);
    if (ministries.length > 0) {
      const ministryCollectionIdentifiers = ministryCollection.map(ministryItem => this.getMinistryIdentifier(ministryItem)!);
      const ministriesToAdd = ministries.filter(ministryItem => {
        const ministryIdentifier = this.getMinistryIdentifier(ministryItem);
        if (ministryCollectionIdentifiers.includes(ministryIdentifier)) {
          return false;
        }
        ministryCollectionIdentifiers.push(ministryIdentifier);
        return true;
      });
      return [...ministriesToAdd, ...ministryCollection];
    }
    return ministryCollection;
  }
}
