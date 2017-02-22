function graphes_notes()
close all;
%order1_exemple1();
order1_transport1();
end

function order1_exemple1()
plot([0 1.2], [0 0], '-k');
xlim([-0.2, 1.2]); ylim([-0.2, 1.2]);
end
function order1_transport1()
% on plote le réseau des caractéristiques
c=1;
figure; hold on;
for s=-5:0.5:4
    fplot(@(x) (x-s)./c, '-k');
end
xlim([-1 5]); ylim([0 5]);
figure; hold on; grid on;
% axe x : 
f=@(s) exp(-s.^2);
for s=-2:0.5:3
    fplot(@(x) x-s, '-k');
end
x=-5:0.1:10;
for t=0:5
    ut=f(x-c.*t);
    plot3(x, t*ones(size(x)), ut, '-b');
end
xlabel('x'); ylabel('c*t'); zlabel('u/u_0');
xlim([-3 8]); ylim([0 5]); zlim([0 1]);
end