exports.defineAutoTests = function () {

    describe('(ACPPlacesMonitor.extensionVersion)', function () {
        it('should exist', function () {
            expect(ACPPlacesMonitor.extensionVersion).toBeDefined();
        });
        it('should be a function', function () {
            expect(typeof ACPPlacesMonitor.extensionVersion === "function").toBe(true);
        });
        it('check if the result is a string', function (done) {
            ACPPlacesMonitor.extensionVersion(function(result) {
                expect(typeof result === "string").toBe(true);
                done();
            }, function() {});
        });
    });
    
    describe('(ACPPlacesMonitor.start)', function () {
        beforeEach(function() {
          spyOn(console, 'log');
        })

        it('should print log to console stating success is not function', function(){
          ACPPlacesMonitor.start("success", function() {})
          expect(console.log).toHaveBeenCalled();
        })

        it('should print log to console stating error is not function', function(){
          ACPPlacesMonitor.start(function() {}, "error")
          expect(console.log).toHaveBeenCalled();
        })
    });

    describe('(ACPPlacesMonitor.stop)', function () {
        beforeEach(function() {
          spyOn(console, 'log');
        })

        var shouldClearData = true;

        it('should print log to console stating success is not function', function(){
          ACPPlacesMonitor.stop(shouldClearData, "success", function() {})
          expect(console.log).toHaveBeenCalled();
        })

        it('should print log to console stating error is not function', function(){
          ACPPlacesMonitor.stop(shouldClearData, function() {}, "error")
          expect(console.log).toHaveBeenCalled();
        })
    });

    describe('(ACPPlacesMonitor.updateLocation)', function () {
        beforeEach(function() {
          spyOn(console, 'log');
        })

        it('should print log to console stating success is not function', function(){
          ACPPlacesMonitor.updateLocation("success", function() {})
          expect(console.log).toHaveBeenCalled();
        })

        it('should print log to console stating error is not function', function(){
          ACPPlacesMonitor.updateLocation(function() {}, "error")
          expect(console.log).toHaveBeenCalled();
        })
    });

    describe('(ACPPlacesMonitor.setRequestLocationPermission)', function () {
        beforeEach(function() {
          spyOn(console, 'log');
        })

        var locationPermission = ACPPlacesMonitor.LocationPermissionWhileUsingTheApp;

        it('should print log to console stating success is not function', function(){
          ACPPlacesMonitor.setRequestLocationPermission(locationPermission, "success", function() {})
          expect(console.log).toHaveBeenCalled();
        })

        it('should print log to console stating error is not function', function(){
          ACPPlacesMonitor.setRequestLocationPermission(locationPermission, function() {}, "error")
          expect(console.log).toHaveBeenCalled();
        })
    });

    describe('(ACPPlacesMonitor.setPlacesMonitorMode)', function () {
        beforeEach(function(){
          spyOn(console, 'log');
        })

        var monitorMode = ACPPlacesMonitor.MonitorModeContinuous;
        
        it('should print log to console stating success is not function', function(){
          ACPPlacesMonitor.setPlacesMonitorMode(monitorMode, "success", function() {})
          expect(console.log).toHaveBeenCalled();
        })

        it('should print log to console stating error is not function', function(){
          ACPPlacesMonitor.setPlacesMonitorMode(monitorMode, function() {}, "error")
          expect(console.log).toHaveBeenCalled();
        })
    });
};