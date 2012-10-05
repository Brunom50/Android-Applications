function Histogram( )

    direc = dir('*.csv');               
    for i=1:length(direc)               
      data(:,:) = csvread(direc(i).name,1);

      figure(i), hist(data(:,1)); 
      title(strrep(strrep(strrep(direc(i).name, '.csv', ''), 'SLOC Count', 'Union All Sources RCG     '), 'SLOC Count', 'Union All Sources RCG     '), 'FontWeight','bold');
      xlabel('SLOC Count'); 
      ylabel('Union All Sources RCG');
      set(gca, 'ActivePositionProperty', 'OuterPosition');
      saveas(gcf,strcat('Histogram-', strrep(direc(i).name, '.csv', ''), '.png'));

    end

end

