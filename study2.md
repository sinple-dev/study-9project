

##### @Cacheable(value = "getProducts", key = "'getProducts' +  #postSearchRequest.getName() + #postSearchRequest.getCategoryId()")


- 메서드 실행 결과는 캐시에 저장
- 동일한 인수로 메서드를 다시 호출하면 캐시에 저장된 결과를 반환
- 캐시에 저장된 결과가 없는 경우에만 메서드가 실행


- **cacheNames(value)**: 캐시 이름을 지정합니다. 여러 개의 캐시 이름을 지정할 수 있습니다.
- **key**: 캐시 키를 지정합니다. SpEL 표현식을 사용할 수 있습니다.
- **condition**: 캐싱 여부를 결정하는 조건을 지정합니다. SpEL 표현식을 사용할 수 있습니다.
    - @Cacheable(cacheNames = "users", condition = "#id > 0")
- **unless**: 캐싱 여부를 결정하는 조건을 지정합니다. SpEL 표현식을 사용할 수 있습니다.
    - @Cacheable(cacheNames = "users", unless = "#result == null")
    - result 가 null 일 경우 데이터를 캐싱함
- **sync**: 캐싱 동기화 여부를 지정합니다. 기본값은 false입니다.
    - 멀티 쓰레드 환경일 경우
    - 성능 저하가 발생할수 있네
- **cacheManager**: 캐시 관리자를 지정합니다. 기본값은 "cacheManager"입니다.

**@EnableCaching**
- 스프링 부트에서 캐싱 기능을 활성화하는 어노테이션입니다.
- 이 어노테이션을 사용하지 않으면 `@Cacheable` 어노테이션이 작동하지 않습니다.
- @EnableCaching(cacheManager = "redisCacheManager") 이렇게 설정 후
- @Cacheable(cacheNames = "users", cacheManager = "redisCacheManager")
- properties 파일에 기본으로 설정하게 되면 (spring.cache.type=redis) cacheManager 생략 가능

- **keyGenerator**: 캐시 키 생성기를 지정합니다. 기본값은 "SimpleKeyGenerator"입니다.
    - @Cacheable(cacheNames = "users", keyGenerator = "customKeyGenerator")
    - 키를 커스텀하게 생성해야 하는 경우
    - 기본은 메서드의 파라미터 값을 사용하여 캐시 키를 생성
- **cacheResolver**: 캐시 리졸버를 지정합니다. 기본값은 "standardCacheResolver"입니다.
- **errorHandler**: 캐싱 오류 처리기를 지정합니다. 기본값은 "SimpleCacheErrorHandler"입니다.
    - @Cacheable(cacheNames = "users", errorHandler = "handleCachingError")
    - 캐싱 처리 과정에서 예외가 발생했을 때 처리

@Cacheable(cacheNames = "users", ttl = 60)
- **ttl** 설정을 통해 만료 시간을 정할수 있음
- 디폴트 시간은 없음. 메모리 부족 문제를 위해 설정하는 것이 중요


### 성능 이슈

**1. 추가적인 오버헤드**
- 캐시 조회 실패 시 DB 조회가 추가적으로 발생하여 응답 시간이 증가합니다.
- 캐시 키 생성, 캐시 조회, DB 조회, 캐시 저장 등의 과정이 발생하며, 이는 추가적인 오버헤드를 발생시킵니다.

**2. 캐시 미스율 영향**
- 캐시 미스율이 높을수록 (캐시에 없는 데이터를 조회하는 경우) 성능 저하가 더 심화됩니다.
- 데이터 접근 패턴에 따라 캐시 미스율이 달라질 수 있으며, 핫 데이터 (자주 접근되는 데이터)의 경우 캐시 미스율이 낮아 성능 향상 효과를 기대할 수 있습니다.

**3. 적절한 사용 시나리오**
- 데이터 변경 빈도가 낮고, 자주 접근되는 데이터의 경우 캐싱을 사용하면 성능 향상 효과를 기대할 수 있습니다.
- 적절한 캐시 만료 시간 설정, 적절한 캐시 크기 설정 등을 통해 캐시 효율성을 높일 수 있습니다.

**4. 캐싱 전략 고려**
- 캐시 먼저 조회하는 방식 외에도, DB 먼저 조회하고 결과를 캐시에 저장하는 방식도 고려할 수 있습니다.
- 데이터 특성, 접근 패턴, 성능 요구사항 등을 고려하여 적절한 캐싱 전략을 선택해야 합니다.

**5. 성능 측정 및 분석**
- 실제 시스템에서 캐싱을 적용하여 성능을 측정하고 분석해야 합니다.
- 캐싱 전후 성능 비교, 캐시 미스율 분석 등을 통해 캐싱 효과를 평가하고 필요하면 전략을 조정해야 합니다.


데이터가 업데이트되었을 때는 캐시를 다시 저장해야 합니다.
- **데이터베이스 트리거**
- **메시징 시스템**
