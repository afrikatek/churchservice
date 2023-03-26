import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMinistry } from '../ministry.model';
import { MinistryService } from '../service/ministry.service';

@Injectable({ providedIn: 'root' })
export class MinistryRoutingResolveService implements Resolve<IMinistry | null> {
  constructor(protected service: MinistryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMinistry | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ministry: HttpResponse<IMinistry>) => {
          if (ministry.body) {
            return of(ministry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
