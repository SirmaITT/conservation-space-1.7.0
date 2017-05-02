import {Configuration, ConfigurationsUpdateEvent, ConfigurationsLoadedEvent} from 'common/application-config';
import {Eventbus} from 'services/eventbus/eventbus';
import {IdocMocks} from 'test/idoc/idoc-mocks';

describe('Configuration service', function () {

  var configurationService;
  beforeEach((done) => {
    var configurationRest = new ConfigurationRestServiceResolve();
    var eventbus = IdocMocks.mockEventBus();
    sinon.spy(eventbus, 'subscribe');
    sinon.spy(eventbus, 'publish');
    configurationService = new Configuration(configurationRest, eventbus);

    configurationService.load().then(() => {
      done();
    }).catch(done);
  });

  it('should call the rest client and store the results', function () {
    expect(configurationService.get('number')).to.equal(123);
  });

  it('should subscribe for configuration update event', () => {
    expect(configurationService.eventbus.subscribe.called).to.be.true;
    expect(configurationService.eventbus.subscribe.getCall(0).args[0]).to.equal(ConfigurationsUpdateEvent);
  });

  it('should publish ConfigurationsLoadedEvent', () => {
    expect(configurationService.eventbus.publish.called).to.be.true;
    expect(configurationService.eventbus.publish.getCall(0).args[0]).to.be.an.instanceof(ConfigurationsLoadedEvent);
  });

  it('should remap configurations on update event', () => {
    var updateHandler = configurationService.eventbus.subscribe.getCall(0).args[1];
    updateHandler([[{
      key: 'state',
      value: 'disabled'
    }]]);
    expect(configurationService.get('state')).to.equal('disabled');
  });

  describe('#getArray(key, separator)', () => {
    it('should return an array of values', () => {
      expect(configurationService.getArray('def')).to.deep.equal(['1', '2', '3']);
    });

    it('should return empty array for missing value', ()=> {
      expect(configurationService.getArray('missing')).to.deep.equal([]);
    });

    it('should use another separator if provided', ()=> {
      expect(configurationService.getArray('123', ';')).to.deep.equal(['a', 'b', 'c']);
    });

    it('should use default separator', ()=> {
      expect(configurationService.getArray('123')).to.deep.equal(['a;b;c']);
    });
  });

  describe('get()', () => {
    it('should return boolean if value is true', () => {
      expect(configurationService.get('booleanTrue')).to.be.true;
    });
    it('should return boolean if value is false', () => {
      expect(configurationService.get('booleanFalse')).to.be.false;
    });
    it('should return boolean if value is string true', () => {
      expect(configurationService.get('booleanTrueString')).to.be.true;
    });
    it('should return boolean if value is string false', () => {
      expect(configurationService.get('booleanFalseString')).to.be.false;
    });
    it('should return number', () => {
      expect(configurationService.get('number')).to.equal(123);
    });
    it('should return number if value is number given as string', () => {
      expect(configurationService.get('numberAsString')).to.equal(123);
    });
    it('should return string', () => {
      expect(configurationService.get('string')).to.equal('string');
    });
    it('should return undefined if key is not found', () => {
      expect(configurationService.get('undefinedkey')).to.be.undefined;
    });
  });

  function mockEventbus() {
    return {
      subscribe: sinon.spy()
    };
  }

});

class ConfigurationRestServiceResolve {

  loadConfigurations() {
    return new Promise((resolve) => {
      resolve({
        data: [
          {
            key: 'booleanTrue',
            value: true
          },
          {
            key: 'booleanFalse',
            value: false
          },
          {
            key: 'booleanTrueString',
            value: 'true'
          },
          {
            key: 'booleanFalseString',
            value: 'false'
          },
          {
            key: 'number',
            value: 123
          },
          {
            key: 'numberAsString',
            value: '123'
          },
          {
            key: 'string',
            value: 'string'
          },
          {
            key: 'def',
            value: '1,2 ,3'
          },
          {
            key: '123',
            value: 'a;b;c'
          }
        ]
      });
    });
  }
}

class ConfigurationRestServiceReject {

  loadConfigurations() {
    return new Promise((resolve, reject) => {
      reject('Error in loading configurations!');
    });
  }

}