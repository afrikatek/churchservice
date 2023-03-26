import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMinistry } from '../ministry.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ministry.test-samples';

import { MinistryService } from './ministry.service';

const requireRestSample: IMinistry = {
  ...sampleWithRequiredData,
};

describe('Ministry Service', () => {
  let service: MinistryService;
  let httpMock: HttpTestingController;
  let expectedResult: IMinistry | IMinistry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MinistryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Ministry', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ministry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ministry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ministry', () => {
      const ministry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ministry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ministry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ministry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ministry', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMinistryToCollectionIfMissing', () => {
      it('should add a Ministry to an empty array', () => {
        const ministry: IMinistry = sampleWithRequiredData;
        expectedResult = service.addMinistryToCollectionIfMissing([], ministry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministry);
      });

      it('should not add a Ministry to an array that contains it', () => {
        const ministry: IMinistry = sampleWithRequiredData;
        const ministryCollection: IMinistry[] = [
          {
            ...ministry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMinistryToCollectionIfMissing(ministryCollection, ministry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ministry to an array that doesn't contain it", () => {
        const ministry: IMinistry = sampleWithRequiredData;
        const ministryCollection: IMinistry[] = [sampleWithPartialData];
        expectedResult = service.addMinistryToCollectionIfMissing(ministryCollection, ministry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministry);
      });

      it('should add only unique Ministry to an array', () => {
        const ministryArray: IMinistry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ministryCollection: IMinistry[] = [sampleWithRequiredData];
        expectedResult = service.addMinistryToCollectionIfMissing(ministryCollection, ...ministryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ministry: IMinistry = sampleWithRequiredData;
        const ministry2: IMinistry = sampleWithPartialData;
        expectedResult = service.addMinistryToCollectionIfMissing([], ministry, ministry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministry);
        expect(expectedResult).toContain(ministry2);
      });

      it('should accept null and undefined values', () => {
        const ministry: IMinistry = sampleWithRequiredData;
        expectedResult = service.addMinistryToCollectionIfMissing([], null, ministry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministry);
      });

      it('should return initial array if no Ministry is added', () => {
        const ministryCollection: IMinistry[] = [sampleWithRequiredData];
        expectedResult = service.addMinistryToCollectionIfMissing(ministryCollection, undefined, null);
        expect(expectedResult).toEqual(ministryCollection);
      });
    });

    describe('compareMinistry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMinistry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMinistry(entity1, entity2);
        const compareResult2 = service.compareMinistry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMinistry(entity1, entity2);
        const compareResult2 = service.compareMinistry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMinistry(entity1, entity2);
        const compareResult2 = service.compareMinistry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
